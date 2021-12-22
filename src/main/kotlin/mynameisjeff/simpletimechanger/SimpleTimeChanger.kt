/*
 * SimpleTimeChanger
 * Copyright (C) 2021 My-Name-Is-Jeff
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package mynameisjeff.simpletimechanger

import gg.essential.universal.UMinecraft
import gg.essential.universal.utils.MCMinecraft
import mynameisjeff.simpletimechanger.commands.SimpleTimeChangerCommand
import mynameisjeff.simpletimechanger.core.Config
import mynameisjeff.simpletimechanger.core.UpdateChecker
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import java.util.*

@Mod(
    modid = SimpleTimeChanger.MODID,
    name = SimpleTimeChanger.MOD_NAME,
    version = SimpleTimeChanger.VERSION,
    acceptedMinecraftVersions = "[1.8.9]",
    clientSideOnly = true,
    modLanguage = "kotlin",
    modLanguageAdapter = "gg.essential.api.utils.KotlinAdapter"
)
object SimpleTimeChanger {

    const val MODID = "simpletimechanger"
    const val MOD_NAME = "SimpleTimeChanger"
    const val VERSION = "1.0.2"
    val mc: MCMinecraft
        get() =  UMinecraft.getMinecraft()

    val isEnabled
        get() = Config.isEnabled
    val worldTime: Int
        get() {
            return if (!Config.useIrlTime) Config.worldTime else {
                val c = GregorianCalendar.getInstance()
                (c.get(Calendar.HOUR_OF_DAY) - 6) * 1000 + c.get(Calendar.MINUTE) * 16
            }
        }

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        Config.preload()
        MinecraftForge.EVENT_BUS.register(this)
    }

    @Mod.EventHandler
    fun onPostInit(event: FMLPostInitializationEvent) {
        SimpleTimeChangerCommand.register()
        if (Config.lastLaunchedVersion != VERSION) Config.differentVersion(Config.lastLaunchedVersion)
        Config.lastLaunchedVersion = VERSION
    }

    @Mod.EventHandler
    fun onLoad(event: FMLLoadCompleteEvent) {
        UpdateChecker.checkUpdate()
    }
}