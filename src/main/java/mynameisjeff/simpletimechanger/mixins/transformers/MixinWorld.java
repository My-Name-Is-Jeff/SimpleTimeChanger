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

package mynameisjeff.simpletimechanger.mixins.transformers;

import mynameisjeff.simpletimechanger.SimpleTimeChanger;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(World.class)
public abstract class MixinWorld implements IBlockAccess {
    @Redirect(method = "getMoonPhase", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/storage/WorldInfo;getWorldTime()J"))
    private long setTimeForMoonPhase(WorldInfo worldInfo) {
        return SimpleTimeChanger.INSTANCE.isEnabled() ? SimpleTimeChanger.INSTANCE.getWorldTime() : worldInfo.getWorldTime();
    }

    @Redirect(method = "getCelestialAngle", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/storage/WorldInfo;getWorldTime()J"))
    private long setTimeForCelestialAngle(WorldInfo worldInfo) {
        return SimpleTimeChanger.INSTANCE.isEnabled() ? SimpleTimeChanger.INSTANCE.getWorldTime() : worldInfo.getWorldTime();
    }
}
