package ru.ssu.refa0217.dmmc.fhcp.backtracking;

import java.util.List;

public class BacktrackingResult {
    private final boolean result;
    private final List<Integer> cycle;
    private final BacktrackingResultCode code;

    public BacktrackingResult(boolean result, List<Integer> cycle) {
        this.result = result;
        this.cycle = cycle;
        code = BacktrackingResultCode.OK;
    }

    public BacktrackingResult(boolean result) {
        this.result = result;
        cycle = null;
        code = BacktrackingResultCode.OK;
    }

    public BacktrackingResult(BacktrackingResultCode code) {
        this.code = code;
        result = false;
        cycle = null;
    }

    public boolean isResult() {
        return result;
    }

    public List<Integer> getCycle() {
        return cycle;
    }

    public boolean isInterrupted() {
        return code == BacktrackingResultCode.INTERRUPTED;
    }

    @Override
    public String toString() {
        return "BacktrackingResult{" +
                "result=" + result +
                ", cycle=" + cycle +
                '}';
    }

    enum BacktrackingResultCode {
        OK,
        INTERRUPTED;
    }
}
