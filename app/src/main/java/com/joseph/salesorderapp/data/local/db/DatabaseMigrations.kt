package com.joseph.salesorderapp.data.local.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE order_summary ADD COLUMN discountAmount REAL NOT NULL DEFAULT 0.0")
    }
}
