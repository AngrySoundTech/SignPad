/**
 * Copyright (C) 2020 AngrySoundTech
 * This file is part of SignPad.
 *
 * SignPad is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SignPad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SignPad.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.github.angarysoundtech.renderer

import io.github.angarysoundtech.SignPad
import io.github.angarysoundtech.handler.InitGuiHandler
import io.github.angarysoundtech.util.decrypt
import io.github.angarysoundtech.util.encrypt
import net.minecraft.client.renderer.tileentity.SignTileEntityRenderer
import net.minecraft.tileentity.SignTileEntity
import net.minecraft.util.text.StringTextComponent

class EncryptedSignTileEntityRenderer : SignTileEntityRenderer() {

    private val regex = Regex("^\\[(.+)]$")

    override fun render(te: SignTileEntity, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int) {
        if (!te.isEditable) {
            val match = regex.find(te.getText(0).unformattedComponentText)

            if (match != null) {
                val key = match.groupValues[1]

                if (SignPad.library.pads.containsKey(key)) {
                    for (i in 1..3) {
                        val decrypted = decrypt(te.getText(i).formattedText, SignPad.library.pads[key]!!)
                        te.setText(i, StringTextComponent(decrypted))
                    }
                }
                te.setText(0, StringTextComponent("<$key>"))
            }
        }

        super.render(te, x, y, z, partialTicks, destroyStage)
    }
}
