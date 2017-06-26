// AIDLServiceInterface.aidl
package com.example.clementvacandard.banqueimages;

// Declare any non-default types here with import statements

interface AIDLServiceInterface {


       Bitmap getImage(in int index);

       int[] getListImage();
}
