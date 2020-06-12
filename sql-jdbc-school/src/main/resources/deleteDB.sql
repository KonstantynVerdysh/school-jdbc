UPDATE pg_database SET datallowconn = 'false' WHERE datname = 'school';
SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'school';
DROP DATABASE school;
DROP USER user1;