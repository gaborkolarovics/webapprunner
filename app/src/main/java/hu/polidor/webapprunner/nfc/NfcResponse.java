package hu.polidor.webapprunner.nfc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NfcResponse implements Serializable {
    private NfcReaderStatus status;

    private List<TagRecord> records;

    public NfcReaderStatus getStatus() {
        return status;
    }

    public void setStatus(final NfcReaderStatus status) {
        this.status = status;
    }

    public List<TagRecord> getRecords() {
        if (records == null) {
            records = new ArrayList<>();
        }
        return records;
    }

    public void setData(final List<TagRecord> records) {
        this.records = records;
    }

    public void addRecord(final TagRecord record) {
        if (records == null) {
            records = new ArrayList<>();
        }
        records.add(record);
    }

}
