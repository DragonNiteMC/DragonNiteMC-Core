package com.dragonite.mc.dnmc.core.managers;

import java.util.List;

public class HelpPages {
    private final List<String> list;
    private boolean StaffPage;

    public HelpPages(List<String> list, boolean staffPage) {
        this.list = list;
        StaffPage = staffPage;
    }

    public List<String> getList() {
        return list;
    }

    public boolean isStaffPage() {
        return StaffPage;
    }

    public void setStaffPage(boolean staffPage) {
        StaffPage = staffPage;
    }
}
