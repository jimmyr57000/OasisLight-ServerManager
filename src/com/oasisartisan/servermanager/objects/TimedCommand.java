package com.oasisartisan.servermanager.objects;

import com.oasisartisan.servermanager.consolecommunication.Printer;
import com.oasisartisan.servermanager.storage.Storage;
import java.io.Serializable;
import java.util.concurrent.Future;
/**
 *
 * @author OasisArtisan
 */
public class TimedCommand implements Serializable {

    private Timing time;
    private String command;
    private transient Future task;

    public TimedCommand(String command, Timing time) {
        this.command = command;
        this.time = time;
        task = null;
    }

    public boolean exec(Server server) {
        if (command.equals("start")) {
            server.start();
        } else if (command.equals("stop")) {
            server.stop();
        } else if (command.equals("restart")) {
            server.restart();
        } else if (command.equals("kill")) {
            server.kill();
        } else if (command.startsWith("send ") && !command.replace("send ", "").isEmpty()) {
            server.sendCommand(command.replace("send ", ""));
        } else if (command.startsWith("backup")) {
            BackupProfile bp = Storage.getBackupProfileList().get(command.replace("backup ", "").trim());
            if (bp != null) {
                server.backup(bp);
            } else {
                server.backup(new BackupProfile("default",null));
            }
        } else {
            Printer.printBackgroundFail(server.getName(), "Failed to execute command \"" + command + "\"");
            return false;
        }
        return true;
    }

    public static boolean isValidCommand(String cmd) {
        try {
            if (cmd.equals("start") || cmd.equals("stop") || cmd.equals("restart") || cmd.equals("kill")
                    || cmd.equals("send") || cmd.equals("backup")) {
                return true;
            }

        } catch (Exception e) {
        }
        return false;
    }

    public Future getTask() {
        return task;
    }

    public void setTask(Future task) {
        this.task = task;
    }

    public Timing getTime() {
        return time;
    }

    public void setTime(Timing time) {
        this.time = time;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
