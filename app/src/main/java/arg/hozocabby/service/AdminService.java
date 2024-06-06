package arg.hozocabby.service;

import arg.hozocabby.database.Database;

public class AdminService {
    private Database db;

    public AdminService(Database db) {
        this.db = db;
    }
}
