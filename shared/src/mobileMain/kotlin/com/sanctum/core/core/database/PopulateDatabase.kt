package com.sanctum.core.core.database

/**
 * Utility interface or expect/actual function to handle migrating the bundled
 * `prayer.db` (seeded via db_seeder.main.kts) into the device's native database directory.
 *
 * Android handles this automatically via `createFromAsset()`, while iOS requires
 * manually moving the .db file from NSBundle to the application document directory
 * before Room instantiates.
 */
expect suspend fun populateDatabaseIfNotExists()
