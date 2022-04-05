package ru.ssu.refa0217.dmmc.fhcp.writer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ResultSaverJob extends Thread {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss SSS");

    private static final String PATH = "C:\\Users\\rafar\\Desktop\\FHCP\\Results\\";
    private static final String FILE_NAME = PATH + "bondy_chvatal.txt";
    private static final String TAB = " --- ";
    private static final String SEC = " sec.";
    private static final String NEW_LINE = "\r\n";

    private final List<Message> messages = new CopyOnWriteArrayList<>();

    @Override
    public void run() {
        while (true) {
            boolean result = handleMessages();
            if (!result) {
                break;
            }
        }
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    private boolean handleMessages() {
        Message message = null;
        if (messages.size() > 0) {
            message = messages.remove(0);
        }
        if (message == null) {
            return true;
        }
        if (message instanceof TerminateMessage) {
            return false;
        }
        try {
            if (message instanceof BondyChvatalSufficiencyConditionMessage) {
                writeBondyChvatalMessage((BondyChvatalSufficiencyConditionMessage) message);
            } else {
                writeHamiltonianCycle((HamiltonianCycleMessage) message);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void writeBondyChvatalMessage(BondyChvatalSufficiencyConditionMessage message) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_NAME, true)))) {
            StringBuilder sb = new StringBuilder();
            sb.append(message.getGraphName())
                    .append(TAB)
                    .append(FORMATTER.format(message.getStartDate()))
                    .append(TAB)
                    .append(FORMATTER.format(message.getEndDate()))
                    .append(TAB)
                    .append(Duration.between(message.getStartDate(), message.getEndDate()).getSeconds())
                    .append(SEC)
                    .append(NEW_LINE);
            bw.write(sb.toString());
        }
    }

    private void writeHamiltonianCycle(HamiltonianCycleMessage message) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(PATH + message.getGraphName())))) {
            StringBuilder sb = new StringBuilder();
            sb.append(message.getGraphName())
                    .append(TAB)
                    .append(FORMATTER.format(message.getStartDate()))
                    .append(TAB)
                    .append(FORMATTER.format(message.getEndDate()))
                    .append(TAB)
                    .append(Duration.between(message.getStartDate(), message.getEndDate()).getSeconds())
                    .append(SEC)
                    .append(NEW_LINE);
            for (Integer node : message.getHamiltonianCycle()) {
                sb.append(node + 1).append(NEW_LINE);
            }
            bw.write(sb.toString());
        }
    }
}
