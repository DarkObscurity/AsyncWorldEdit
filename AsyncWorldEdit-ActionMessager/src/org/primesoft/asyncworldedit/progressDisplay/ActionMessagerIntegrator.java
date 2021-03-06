/*
 * AsyncWorldEdit a performance improvement plugin for Minecraft WorldEdit plugin.
 * Copyright (c) 2015, SBPrime <https://github.com/SBPrime/>
 * Copyright (c) AsyncWorldEdit contributors
 *
 * All rights reserved.
 *
 * Redistribution in source, use in source and binary forms, with or without
 * modification, are permitted free of charge provided that the following 
 * conditions are met:
 *
 * 1.  Redistributions of source code must retain the above copyright notice, this
 *     list of conditions and the following disclaimer.
 * 2.  Redistributions of source code, with or without modification, in any form
 *     other then free of charge is not allowed,
 * 3.  Redistributions of source code, with tools and/or scripts used to build the 
 *     software is not allowed,
 * 4.  Redistributions of source code, with information on how to compile the software
 *     is not allowed,
 * 5.  Providing information of any sort (excluding information from the software page)
 *     on how to compile the software is not allowed,
 * 6.  You are allowed to build the software for your personal use,
 * 7.  You are allowed to build the software using a non public build server,
 * 8.  Redistributions in binary form in not allowed.
 * 9.  The original author is allowed to redistrubute the software in bnary form.
 * 10. Any derived work based on or containing parts of this software must reproduce
 *     the above copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials provided with the
 *     derived work.
 * 11. The original author of the software is allowed to change the license
 *     terms or the entire license of the software as he sees fit.
 * 12. The original author of the software is allowed to sublicense the software
 *     or its parts using any license terms he sees fit.
 * 13. By contributing to this project you agree that your contribution falls under this
 *     license.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.primesoft.asyncworldedit.progressDisplay;

import net.sneling.actionmessager.ActionMessager;

import javax.annotation.Nonnull;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.primesoft.asyncworldedit.api.playerManager.IPlayerEntry;
import org.primesoft.asyncworldedit.api.progressDisplay.IProgressDisplay;
import org.primesoft.asyncworldedit.api.progressDisplay.IProgressDisplayManager;

/**
 * @author Weby@we-bb.com <Nicolas Glassey>
 * @version 1.0.0
 * @since 30/04/15
 */
public class ActionMessagerIntegrator implements IProgressDisplay {
    private final Server m_server;
    private final IProgressDisplayManager m_progressManager;
    private final ActionMessager am;

    public ActionMessagerIntegrator(Plugin plugin, IProgressDisplayManager progressDisplayManager) {
        m_progressManager = progressDisplayManager;
        am = (ActionMessager) plugin.getServer().getPluginManager().getPlugin("ActionMessager");
        m_server = plugin.getServer();
    }

    @Override
    public void disableMessage(@Nonnull IPlayerEntry player) {
        if (am != null) {
            am.sendMessage(m_server.getPlayer(player.getUUID()), "");
        }
    }

    @Override
    public void setMessage(IPlayerEntry player, int jobs, int blocks, int maxBlocks, double time, double speed, double percentage) {
        if (am == null) {
            return;
        }
            String block_full = "█";
            String block_half = "░";
            int barAmount = 20;

            String message = m_progressManager.formatMessage(jobs, speed, time);

            if (message == null) {
                message = "";
            }
            if (percentage < 0) {
                percentage = 0;
            }
            if (percentage > 100) {
                percentage = 100;
            }

            int increment = 100 / barAmount;
            int darkAmount = (int) percentage / increment;
            int lightAmount = barAmount - darkAmount;

            String bars = "";
            for (int i = 0; i < darkAmount; i++) {
                bars += block_full;
            }
            for (int i = 0; i < lightAmount; i++) {
                bars += block_half;
            }

            message += " : " + bars + " " + (int) percentage + "%";

            am.sendMessage(m_server.getPlayer(player.getUUID()), message);
    }

    @Override
    public String getName() {
        return "Action Messager";
    }

}
