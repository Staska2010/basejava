package ru.topjava.basejava;

import ru.topjava.basejava.model.SectionType;

public class EnumTest {
    public static void main(String[] args) {
        for(SectionType type : SectionType.values()) {
            System.out.println(type.getTitle());
        }
    }
}
