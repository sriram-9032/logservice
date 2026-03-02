package com.dtt.logs.dto;

import java.time.Month;
import java.util.Comparator;
import java.util.Locale;

public class MonthYearComparator implements Comparator<String> {

    @Override
    public int compare(String a, String b) {
        try {
            String[] aParts = a.trim().split("\\s+");
            String[] bParts = b.trim().split("\\s+");

            // Defensive parsing (handles lowercase and uppercase month names)
            Month aMonth = Month.valueOf(aParts[0].substring(0, 1).toUpperCase(Locale.ENGLISH)
                    + aParts[0].substring(1).toLowerCase(Locale.ENGLISH));
            Month bMonth = Month.valueOf(bParts[0].substring(0, 1).toUpperCase(Locale.ENGLISH)
                    + bParts[0].substring(1).toLowerCase(Locale.ENGLISH));

            int aYear = Integer.parseInt(aParts[1]);
            int bYear = Integer.parseInt(bParts[1]);

            if (aYear != bYear) return Integer.compare(aYear, bYear);
            return Integer.compare(aMonth.getValue(), bMonth.getValue());
        } catch (Exception e) {
            return a.compareToIgnoreCase(b);
        }
    }
}
