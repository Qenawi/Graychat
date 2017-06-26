package com.example.qenawi.am.Contract;

import com.example.qenawi.am.modles.users_data_modle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by QEnawi on 4/11/2017.
 */

public  class ContractAcc
{
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    public ContractAcc(){}
    public  users_data_modle get_username()
    {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser==null)return null;
        return  new users_data_modle(mFirebaseUser.getEmail(),mFirebaseUser.getDisplayName(),mFirebaseUser.getPhotoUrl().toString());
    }
}
