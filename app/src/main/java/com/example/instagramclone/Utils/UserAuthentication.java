package com.example.instagramclone.Utils;

import com.google.firebase.auth.FirebaseAuth;

public class UserAuthentication {
    public static String userId;
    public static boolean UserExists(){
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            return true;
        }
        else return false;
    }
}
