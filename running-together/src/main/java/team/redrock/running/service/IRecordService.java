package team.redrock.running.service;

import team.redrock.running.vo.Record;

//@Service
public interface IRecordService {
    void addRecord(String table, Record record);
}
