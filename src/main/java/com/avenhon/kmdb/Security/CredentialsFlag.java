package com.avenhon.kmdb.Security;

public class CredentialsFlag {
    private final String[] args;

    public CredentialsFlag(String[] args) {
        this.args = args;
    }

    public void printCredentials() {
        if (args.length > 1) {
            throw new RuntimeException("Must be no more than 1 argument");
        } else if (args.length == 1 && !(args[0].equals("-c") || args[0].equals("--credentials"))) {
            throw new RuntimeException("Unknown flag");
        } else if (args.length == 1) {
            System.out.println("\n--------------------------------------------------------------------------------------------");
            System.out.println("\nProject created by:");
            System.out.println("""
                   
                                  _                                                                       \s
                                 u                                     .uef^"                             \s
                                88Nu.   u.                u.    u.   :d88E              u.      u.    u.  \s
                         u     '88888.o888c      .u     x@88k u@88c. `888E        ...ue888b   x@88k u@88c.\s
                      us888u.   ^8888  8888   ud8888.  ^"8888""8888"  888E .z8k   888R Y888r ^"8888""8888"\s
                   .@88 "8888"   8888  8888 :888'8888.   8888  888R   888E~?888L  888R I888>   8888  888R \s
                   9888  9888    8888  8888 d888 '88%"   8888  888R   888E  888E  888R I888>   8888  888R \s
                   9888  9888    8888  8888 8888.+"      8888  888R   888E  888E  888R I888>   8888  888R \s
                   9888  9888   .8888b.888P 8888L        8888  888R   888E  888E u8888cJ888    8888  888R \s
                   9888  9888    ^Y8888*""  '8888c. .+  "*88*" 8888"  888E  888E  "*888*P"    "*88*" 8888"\s
                   "888*""888"     `Y"       "88888%      ""   'Y"   m888N= 888>    'Y"         ""   'Y"  \s
                    ^Y"   ^Y'                  "YP'                   `Y"   888                           \s
                                                                           J88"                           \s
                                                                           @%                             \s
                                                                         :"                               \s
                   """);
            System.out.println("--------------------------------------------------------------------------------------------");
            System.exit(0);
        }
    }
}
