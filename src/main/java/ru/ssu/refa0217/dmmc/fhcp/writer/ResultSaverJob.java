package ru.ssu.refa0217.dmmc.fhcp.writer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ResultSaverJob extends Thread {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss SSS");

    private static final String PATH = "C:\\Users\\rafar\\Desktop\\FHCP\\Results\\";
    private static final String HYBRID_PATH = PATH + "hybrid\\";
    private static final String FILE_NAME = PATH + "bondy_chvatal.txt";
    private static final String TAB = " --- ";
    private static final String NEW_LINE = "\r\n";
    private static final String COMMA = ", ";

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
            } else if (message instanceof HamiltonianCycleMessage) {
                writeHamiltonianCycle((HamiltonianCycleMessage) message);
            } else if (message instanceof HybridHamiltonianCycleMessage) {
                writeHybridHamiltonianCycle((HybridHamiltonianCycleMessage) message);
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
                    .append(FORMATTER.format(Instant.ofEpochMilli(message.getWatch().getStartTime())
                            .atZone(ZoneId.systemDefault()).toLocalDateTime()))
                    .append(TAB)
                    .append(FORMATTER.format(Instant.ofEpochMilli(message.getWatch().getStopTime())
                            .atZone(ZoneId.systemDefault()).toLocalDateTime()))
                    .append(TAB)
                    .append(message.getWatch().formatTime())
                    .append(NEW_LINE);
            bw.write(sb.toString());
        }
    }

    private void writeHamiltonianCycle(HamiltonianCycleMessage message) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(PATH + message.getGraphName())))) {
            StringBuilder sb = new StringBuilder();
            sb.append(message.getGraphName())
                    .append(TAB)
                    .append(FORMATTER.format(Instant.ofEpochMilli(message.getWatch().getStartTime())
                            .atZone(ZoneId.systemDefault()).toLocalDateTime()))
                    .append(TAB)
                    .append(FORMATTER.format(Instant.ofEpochMilli(message.getWatch().getStopTime())
                            .atZone(ZoneId.systemDefault()).toLocalDateTime()))
                    .append(TAB)
                    .append(message.getWatch().formatTime())
                    .append(NEW_LINE);
            for (Integer node : message.getHamiltonianCycle()) {
                sb.append(node + 1).append(NEW_LINE);
            }
            bw.write(sb.toString());
        }
    }

    private void writeHybridHamiltonianCycle(HybridHamiltonianCycleMessage message) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(HYBRID_PATH + message.getGraphName())))) {
            StringBuilder sb = new StringBuilder();
            sb.append(message.getGraphName())
                    .append(TAB)
                    .append(FORMATTER.format(Instant.ofEpochMilli(message.getWatch().getStartTime())
                            .atZone(ZoneId.systemDefault()).toLocalDateTime()))
                    .append(TAB)
                    .append(FORMATTER.format(Instant.ofEpochMilli(message.getWatch().getStopTime())
                            .atZone(ZoneId.systemDefault()).toLocalDateTime()))
                    .append(TAB)
                    .append(message.getWatch().formatTime())
                    .append(NEW_LINE);

            sb.append("Path:").append(NEW_LINE);
            for (Integer node : message.getPathAndCycle().getLeft()) {
                sb.append(node + 1).append(COMMA);
            }
            sb.delete(sb.length() - 2, sb.length());
            sb.append(NEW_LINE);

            sb.append("Cycle:").append(NEW_LINE);
            if (message.getPathAndCycle().getRight() != null) {
                for (Integer node : message.getPathAndCycle().getRight()) {
                    sb.append(node + 1).append(COMMA);
                }
                sb.delete(sb.length() - 2, sb.length());
            }

            bw.write(sb.toString());
        }
    }
}
