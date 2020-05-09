package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6;

public class Task17 {

    public static class Employee implements Comparable<Employee> {

        @Override
        public int compareTo(Employee o) {
            return 0;
        }

    }

//  public class com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6.Task17$Employee implements java.lang.Comparable<com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6.Task17$Employee> {
//  public com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6.Task17$Employee();
//  public int compareTo(com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6.Task17$Employee);
//  public int compareTo(java.lang.Object); - !!! bridge method !!!
//}


//    verbose
//    Constant pool:
//   #1 = Methodref          #4.#19         // java/lang/Object."<init>":()V
//   #2 = Class              #21            // com/amairovi/horstmann_java_se_9_for_the_impatient/chapter_6/Task17$Employee
//   #3 = Methodref          #2.#22         // com/amairovi/horstmann_java_se_9_for_the_impatient/chapter_6/Task17$Employee.compareTo:(Lcom/amairovi/horstmann_java_se_9_for_the_impatient/chapter_6/Task17$Employee;)I
// ...
//  public int compareTo(com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6.Task17$Employee);
//    descriptor: (Lcom/amairovi/horstmann_java_se_9_for_the_impatient/chapter_6/Task17$Employee;)I
//    flags: ACC_PUBLIC
//    Code:
//      stack=1, locals=2, args_size=2
//         0: iconst_0
//         1: ireturn
//      LineNumberTable:
//        line 9: 0
//
//  public int compareTo(java.lang.Object);
//    descriptor: (Ljava/lang/Object;)I
//    flags: ACC_PUBLIC, ACC_BRIDGE, ACC_SYNTHETIC
//    Code:
//      stack=2, locals=2, args_size=2
//         0: aload_0
//         1: aload_1
//         2: checkcast     #2                  // class com/amairovi/horstmann_java_se_9_for_the_impatient/chapter_6/Task17$Employee
//         5: invokevirtual #3                  // Method compareTo:(Lcom/amairovi/horstmann_java_se_9_for_the_impatient/chapter_6/Task17$Employee;)I
//         8: ireturn
//      LineNumberTable:
//        line 5: 0
//}
//Signature: #16                          // Ljava/lang/Object;Ljava/lang/Comparable<Lcom/amairovi/horstmann_java_se_9_for_the_impatient/chapter_6/Task17$Employee;>;
//SourceFile: "Task17.java"

}
