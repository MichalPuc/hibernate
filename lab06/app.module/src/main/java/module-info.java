module app.module {
    requires tables.module;
    requires java.sql;
    requires java.naming;
    requires org.hibernate.orm.core;
    requires sqlite.dialect;
}