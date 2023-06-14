package com.dotmarketing.common.db;

import com.dotcms.util.IntegrationTestInitService;
import com.dotmarketing.db.DbConnectionFactory;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.workflows.business.BaseWorkflowIntegrationTest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class DotDatabaseMetaDataTest extends BaseWorkflowIntegrationTest {

    @BeforeClass
    public static void prepare() throws Exception {
        //Setting web app environment

        IntegrationTestInitService.getInstance().init();

    }

    @Test
    public void findForeignKeysTest() throws DotDataException, DotSecurityException {

        //alter table contentlet add constraint FK_structure_inode foreign key (structure_inode) references structure(inode);
        final ForeignKey foreignKey        = new DotDatabaseMetaData().findForeignKeys
                ("contentlet", "structure",
                        Arrays.asList("structure_inode"), Arrays.asList("inode"));

        assertNotNull(foreignKey);
        Assert.assertEquals("FK_structure_inode".toLowerCase(), foreignKey.getForeignKeyName().toLowerCase());
    }

    @Test
    public void getForeignKeysTest() throws DotDataException, DotSecurityException {

        //alter table contentlet add constraint FK_structure_inode foreign key (structure_inode) references structure(inode);
        final List<ForeignKey> foreignKeys = new DotDatabaseMetaData().getForeignKeys("contentlet");

        assertNotNull(foreignKeys);
        Assert.assertTrue(foreignKeys.size()>0);
    }

    @Test
    public void tableExistsTest() throws DotDataException, DotSecurityException, SQLException {

        //alter table contentlet add constraint FK_structure_inode foreign key (structure_inode) references structure(inode);
        try (final Connection connection = DbConnectionFactory.getConnection()) {
            Assert.assertTrue(new DotDatabaseMetaData().tableExists(connection, "contentlet"));
        }
    }

    @Test
    public void tableNotExistsTest() throws DotDataException, DotSecurityException, SQLException {

        //alter table contentlet add constraint FK_structure_inode foreign key (structure_inode) references structure(inode);
        try (final Connection connection = DbConnectionFactory.getConnection()) {
            Assert.assertFalse(new DotDatabaseMetaData().tableExists(connection, "xxx"));
            Assert.assertTrue(new DotDatabaseMetaData().tableExists(connection, "contentlet"));
        }
    }

    @Test
    public void getColumnNames() throws DotDataException, DotSecurityException, SQLException {

        //alter table contentlet add constraint FK_structure_inode foreign key (structure_inode) references structure(inode);
        try (final Connection connection = DbConnectionFactory.getConnection()) {
            final Set<String> strings = new DotDatabaseMetaData().getColumnNames(connection, "contentlet");

            final Set<String> lowerStrings = strings.stream().map(String::toLowerCase)
                    .collect(Collectors.toSet());

            Assert.assertTrue(lowerStrings.contains("inode"));
            Assert.assertTrue(lowerStrings.contains("show_on_menu"));
            Assert.assertTrue(lowerStrings.contains("title"));
            Assert.assertTrue(lowerStrings.contains("mod_date"));
        }
    }

    // the result should not be null
    @Test
    public void getModifiedColumnLength() throws SQLException, DotDataException {
        final String colName = "locked_by";
        final String tblName = "contentlet_version_info";

        if (DbConnectionFactory.isPostgres()){
            final String query = "alter table "+tblName+" alter column locked_by type varchar (100);";
            final DotConnect dotConnect = new DotConnect();
            dotConnect.executeStatement(query);
        }

        final Map<String, String> result = new DotDatabaseMetaData().getModifiedColumnLength(tblName, colName);
        assertNotNull(result);
    }

}
