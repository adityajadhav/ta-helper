package com.cs442.team4.tahelper.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by sowmyaparameshwara on 10/30/16.
 */

public class ModuleEntity {



    private static HashMap<String,ArrayList<AssignmentEntity>> moduleToAssignmets = new HashMap<>();


    public ModuleEntity(){

    }


    public static void addModule(String moduleName){
        if(moduleToAssignmets.get(moduleName)==null) {
            ArrayList<AssignmentEntity> assignmentList = new ArrayList<AssignmentEntity>();
            moduleToAssignmets.put(moduleName, assignmentList);
        }
    }


    public static void addAssignments(String moduleName,AssignmentEntity assignmentEntity){
        ArrayList<AssignmentEntity> assignmentList =moduleToAssignmets.get(moduleName);
        if(!assignmentList.contains(assignmentEntity)) {
            assignmentList.add(assignmentEntity);
        }
    }

    public static ArrayList<AssignmentEntity> getAssignmentList(String name){
        return moduleToAssignmets.get(name);
    }

    public static void removeModule(String name){
        moduleToAssignmets.remove(name);
    }

    public  static void editModule(String originalName,String newName){
        ArrayList<AssignmentEntity> assignmentOldList =moduleToAssignmets.get(originalName);
        ArrayList<AssignmentEntity> assignmentNewList = new ArrayList<AssignmentEntity>();
        assignmentNewList.addAll(assignmentOldList);
         moduleToAssignmets.put(newName,assignmentNewList);
        moduleToAssignmets.remove(originalName);
    }

    public static void removeAssignmentFromModule(String moduleName,String assignmentName){
        if(moduleToAssignmets.get(moduleName)!=null){
            ArrayList<AssignmentEntity> assignmentList = moduleToAssignmets.get(moduleName);
            for(int i = 0 ; i< assignmentList.size();i++){
                AssignmentEntity assignmentEntity = assignmentList.get(i);
                if(assignmentEntity.getAssignmentName().equals(assignmentName)){
                    assignmentList.remove(assignmentEntity);
                    break;
                }
            }
        }
    }



}
