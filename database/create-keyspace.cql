create keyspace if not exists localization with replication = {
    'class' : 'SimpleStrategy',
    'replication_factor' : 1
};
