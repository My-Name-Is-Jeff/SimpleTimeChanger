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

package mynameisjeff.simpletimechanger.core

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import java.io.File

object Config : Vigilant(File("./config/simpletimechanger.toml"), "Time Changer") {

    @Property(
        type = PropertyType.TEXT, name = "Last Launched Version",
        description = "The last version of SimpleTimeChanger that was launched.",
        category = "General", subcategory = "Hidden",
        hidden = true
    )
    var lastLaunchedVersion = ""

    @Property(
        type = PropertyType.SWITCH, name = "Enabled",
        description = "Whether or not the TimeChanger should function.",
        category = "General"
    )
    var isEnabled = false

    @Property(
        type = PropertyType.SWITCH, name = "Use IRL Time",
        description = "Sets the time of the world to be based on your timezone",
        category = "Time"
    )
    var useIrlTime = false

    @Property(
        type = PropertyType.SLIDER, name = "World Time",
        description = "The time to set the world to.",
        category = "Time",
        max = 23999
    )
    var worldTime = 0

    init {
        initialize()
    }

    fun differentVersion(old: String) {

    }
}