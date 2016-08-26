package com.github.rkpunjal.sqlsafe;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SqlSafeUtilTest {

    private static final String SPACES_REGEX = "[\\s|\\r|\\n|\\t]";
    private static final String EMPTY = "";

    @Test
    public void testWithBadData(){
        String[] maliciousDataSamples = {
                "select adf from abc",
                "insert into abcd",
                "update abcd",
                "delete from abcd",
                "upsert abcd",
                "call abcd",
                "rollback ",
                "create table abc",
                "drop table",
                "drop view",
                "alter table abc",
                "truncate table abc",
                "desc abc",
        };

        for(String maliciousPart : maliciousDataSamples){
            testUnSafeWithAllVariations(maliciousPart);
        }


        String[] sqlDisruptiveDataSamples = {
                "--",
                "/*",
                "*/",
                ";",
                "someone -- abcd",
                "abcd /* adf */ adf",
        };


        for(String desruptivePart : sqlDisruptiveDataSamples){
            testForPurelyUnSafeDataWithAllVariations(desruptivePart);
        }



    }

    @Test
    public void testWithGoodData(){
        String[] safeDataSamples = {
                "12",
                "abcd123",
                "123abcd",
                "abcd",
        };

        for(String safeData : safeDataSamples){
            assertTrue("Failed to qualify this as SQL-injection safe data : " + safeData,
                    SqlSafeUtil.isSqlInjectionSafe(safeData)
            );
        }

    }


    private void testUnSafeWithAllVariations(String maliciousPart) {
        String prefix = "some-Data-prefix";
        String suffix = "some-Data-suffix";
        String space = " ";

        String maliciousData = "";
        String safeData = "";

        maliciousData = prefix + space + maliciousPart + space + suffix;

        assertFalse("Failed to detect SQL-unsafe data : " + maliciousData,
                SqlSafeUtil.isSqlInjectionSafe(maliciousData)
        );

        assertFalse("Failed to detect SQL-unsafe data : " + maliciousData.toUpperCase(),
                SqlSafeUtil.isSqlInjectionSafe(maliciousData.toUpperCase())
        );

        safeData = prefix + maliciousPart + suffix;

        assertTrue("Failed to qualify this as SQL-injection safe data : " + safeData,
                SqlSafeUtil.isSqlInjectionSafe(safeData)
        );

        safeData = removeAllSpaces(maliciousData);
        assertTrue("Failed to qualify this as SQL-injection safe data : " + safeData,
                SqlSafeUtil.isSqlInjectionSafe(safeData)
        );

        prefix = "";
        suffix = "";
        maliciousData = prefix + maliciousPart + suffix;

        assertFalse("Failed to detect SQL-unsafe data : " + maliciousData,
                SqlSafeUtil.isSqlInjectionSafe(maliciousData)
        );


        safeData = removeAllSpaces(maliciousData);
        assertTrue("Failed to qualify this as SQL-injection safe data : " + safeData,
                SqlSafeUtil.isSqlInjectionSafe(safeData)
        );

    }
    private void testForPurelyUnSafeDataWithAllVariations(String maliciousPart) {
        String prefix = "some-Data-prefix";
        String suffix = "some-Data-suffix";
        String space = " ";

        String maliciousData = "";
        String safeData = "";

        maliciousData = prefix + space + maliciousPart + space + suffix;

        assertFalse("Failed to detect SQL-unsafe data : " + maliciousData,
                SqlSafeUtil.isSqlInjectionSafe(maliciousData)
        );

        assertFalse("Failed to detect SQL-unsafe data : " + maliciousData.toUpperCase(),
                SqlSafeUtil.isSqlInjectionSafe(maliciousData.toUpperCase())
        );

        assertFalse("Failed to detect SQL-unsafe data : " + removeAllSpaces(maliciousData),
                SqlSafeUtil.isSqlInjectionSafe(removeAllSpaces(maliciousData))
        );

        prefix = "";
        suffix = "";
        maliciousData = prefix + maliciousPart + suffix;

        assertFalse("Failed to detect SQL-unsafe data : " + maliciousData,
                SqlSafeUtil.isSqlInjectionSafe(maliciousData)
        );

        assertFalse("Failed to detect SQL-unsafe data : " + removeAllSpaces(maliciousData),
                SqlSafeUtil.isSqlInjectionSafe(removeAllSpaces(maliciousData))
        );

    }

    private String removeAllSpaces(String str){
        Pattern pattern = Pattern.compile(SPACES_REGEX);
        Matcher matcher = pattern.matcher(str);
        return matcher.replaceAll(EMPTY);
    }

}


