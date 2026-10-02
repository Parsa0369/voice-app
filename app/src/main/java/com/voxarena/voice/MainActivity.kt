package com.voxarena.voice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { VoxArenaApp() }
    }
}

private val Night = Color(0xFF080B14)
private val Surface = Color(0xFF121827)
private val Neon = Color(0xFF7C5CFF)
private val Mint = Color(0xFF40E2B7)

@Composable
fun VoxArenaApp() {
    var micOn by remember { mutableStateOf(true) }
    var speakerOn by remember { mutableStateOf(true) }
    var activeTab by remember { mutableStateOf(0) }
    MaterialTheme(colorScheme = darkColorScheme(primary = Neon, surface = Surface)) {
        Surface(color = Night, modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 16.dp)) {
                TopBar()
                Spacer(Modifier.height(24.dp))
                when (activeTab) {
                    0 -> RoomContent(micOn, speakerOn, { micOn = !micOn }, { speakerOn = !speakerOn })
                    1 -> DiscoverContent()
                    else -> ProfileContent()
                }
                Spacer(Modifier.weight(1f))
                BottomBar(activeTab) { activeTab = it }
            }
        }
    }
}

@Composable private fun TopBar() = Row(verticalAlignment = Alignment.CenterVertically) {
    Box(Modifier.size(42.dp).clip(RoundedCornerShape(14.dp)).background(Brush.linearGradient(listOf(Neon, Color(0xFFB54CFF)))), contentAlignment = Alignment.Center) {
        Icon(Icons.Default.GraphicEq, null, tint = Color.White)
    }
    Spacer(Modifier.width(12.dp))
    Column { Text("VOX ARENA", color = Color.White, fontWeight = FontWeight.Black, letterSpacing = 1.sp); Text("VOICE FOR PLAYERS", color = Color(0xFF8D96AD), fontSize = 10.sp, letterSpacing = 1.sp) }
    Spacer(Modifier.weight(1f))
    Box(Modifier.size(38.dp).clip(CircleShape).background(Surface), contentAlignment = Alignment.Center) { Icon(Icons.Default.NotificationsNone, null, tint = Color.White) }
}

@Composable private fun RoomContent(micOn: Boolean, speakerOn: Boolean, toggleMic: () -> Unit, toggleSpeaker: () -> Unit) {
    Text("اتاق فعال", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
    Text("NIGHT RAID  •  5 / 8 بازیکن", color = Color(0xFF8D96AD), fontSize = 13.sp)
    Spacer(Modifier.height(18.dp))
    ConnectionCard()
    Spacer(Modifier.height(22.dp))
    Text("اسکواد شما", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(12.dp))
    Player("شما", "AR", Mint, "در حال صحبت", true)
    Player("Nima.exe", "NE", Color(0xFFFFB45E), "آماده", false)
    Player("Sara V", "SV", Color(0xFFEF6C9A), "در لابی", false)
    Player("Kian_FPS", "KF", Color(0xFF5A9CFF), "بی‌صدا", false)
    Spacer(Modifier.height(18.dp))
    Text("کنترل سریع", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(12.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        QuickButton(if (micOn) Icons.Default.Mic else Icons.Default.MicOff, if (micOn) "میکروفون روشن" else "میکروفون خاموش", micOn, toggleMic, Modifier.weight(1f))
        QuickButton(if (speakerOn) Icons.Default.VolumeUp else Icons.Default.VolumeOff, "صدای بازی", speakerOn, toggleSpeaker, Modifier.weight(1f))
    }
    Spacer(Modifier.height(18.dp))
    val color by animateColorAsState(if (micOn) Neon else Color(0xFF303748), label = "ptt")
    Box(Modifier.fillMaxWidth().height(64.dp).shadow(16.dp, RoundedCornerShape(20.dp)).clip(RoundedCornerShape(20.dp)).background(color).clickable { toggleMic() }, contentAlignment = Alignment.Center) {
        Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.KeyboardVoice, null, tint = Color.White); Spacer(Modifier.width(10.dp)); Text(if (micOn) "برای صحبت نگه دارید" else "میکروفون را فعال کنید", color = Color.White, fontWeight = FontWeight.Bold) }
    }
}

@Composable private fun ConnectionCard() = Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(Brush.linearGradient(listOf(Color(0xFF1C2540), Surface))).border(1.dp, Color(0xFF313D5B), RoundedCornerShape(22.dp)).padding(18.dp)) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(52.dp).clip(CircleShape).background(Color(0x1A40E2B7)), contentAlignment = Alignment.Center) { Icon(Icons.Default.NetworkCheck, null, tint = Mint) }
        Spacer(Modifier.width(14.dp)); Column(Modifier.weight(1f)) { Text("اتصال پایدار", color = Color.White, fontWeight = FontWeight.Bold); Text("سرور تهران  •  کیفیت Ultra", color = Color(0xFF9EA8C2), fontSize = 12.sp) }
        Column(horizontalAlignment = Alignment.End) { Text("24 ms", color = Mint, fontWeight = FontWeight.Black, fontSize = 18.sp); Text("تاخیر", color = Color(0xFF8D96AD), fontSize = 11.sp) }
    }
}

@Composable private fun Player(name: String, initials: String, color: Color, state: String, speaking: Boolean) = Row(Modifier.fillMaxWidth().padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
    Box(Modifier.size(42.dp).clip(CircleShape).background(color), contentAlignment = Alignment.Center) { Text(initials, color = Night, fontWeight = FontWeight.Black, fontSize = 12.sp) }
    Spacer(Modifier.width(12.dp)); Column(Modifier.weight(1f)) { Text(name, color = Color.White, fontWeight = FontWeight.SemiBold); Text(state, color = if (speaking) Mint else Color(0xFF8490AA), fontSize = 12.sp) }
    if (speaking) Icon(Icons.Default.GraphicEq, null, tint = Mint) else Icon(Icons.Default.MoreHoriz, null, tint = Color(0xFF8490AA))
}

@Composable private fun QuickButton(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, active: Boolean, click: () -> Unit, modifier: Modifier) = Column(modifier.clip(RoundedCornerShape(16.dp)).background(if (active) Color(0xFF202B48) else Surface).clickable { click() }.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) { Icon(icon, null, tint = if (active) Neon else Color(0xFF8490AA)); Spacer(Modifier.height(6.dp)); Text(text, color = Color.White, fontSize = 11.sp, textAlign = TextAlign.Center) }

@Composable private fun DiscoverContent() = Box(Modifier.fillMaxWidth().padding(top = 80.dp), contentAlignment = Alignment.Center) { Text("اتاق‌های محبوب به‌زودی", color = Color.White) }
@Composable private fun ProfileContent() = Box(Modifier.fillMaxWidth().padding(top = 80.dp), contentAlignment = Alignment.Center) { Text("پروفایل گیمر", color = Color.White) }

@Composable private fun BottomBar(active: Int, select: (Int) -> Unit) = Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(Surface).padding(vertical = 11.dp), horizontalArrangement = Arrangement.SpaceAround) {
    listOf(Icons.Default.Headset to "اتاق", Icons.Default.Explore to "کشف", Icons.Default.Person to "پروفایل").forEachIndexed { index, item ->
        Column(Modifier.clickable { select(index) }.padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) { Icon(item.first, null, tint = if (active == index) Neon else Color(0xFF77819A)); Text(item.second, color = if (active == index) Color.White else Color(0xFF77819A), fontSize = 10.sp) }
    }
}
