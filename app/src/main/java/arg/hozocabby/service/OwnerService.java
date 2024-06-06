package arg.hozocabby.service;

import arg.hozocabby.database.Database;

public class OwnerService {
    private Database db;

    public OwnerService(Database db) {
        this.db = db;
    }
}
