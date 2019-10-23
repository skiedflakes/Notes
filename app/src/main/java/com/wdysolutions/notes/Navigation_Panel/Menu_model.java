package com.wdysolutions.notes.Navigation_Panel;

public class Menu_model {

    String menu_id;
    String level;
    String parent;
    String menu_name;
    String program_type;

    public Menu_model(String menu_id, String level, String parent,String program_type, String menu_name){
        this.level = level;
        this.menu_id = menu_id;
        this.parent = parent;
        this.program_type=program_type;
        this.menu_name = menu_name;
    }

    public String getProgram_type() {
        return program_type;
    }

    public String getLevel() {
        return level;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public String getParent() {
        return parent;
    }
}
