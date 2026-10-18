package com.example.gymmanager;

import java.util.ArrayList;

public class MemberStorage {

    public static ArrayList<Member> members = new ArrayList<>();
    private static int nextId =1;

    public static void add(Member member) {
        member.id = nextId++;
        members.add(member);
    }

    public static void update(Member updatedMember) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).id == updatedMember.id) {
                members.set(i, updatedMember);
                return;
            }
        }
    }

    public static void delete(int id) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).id == id) {
                members.remove(i);
                return;
            }
        }
    }

    public static Member getById(int id) {
        for (Member member : members) {
            if (member.id == id) {
                return member;
            }
        }
        return null;
    }

    public static double getTotalIncome() {
        double total = 0;
        for (Member member : members) {
            total += member.totalFee;
        }
        return total;
    }
}
