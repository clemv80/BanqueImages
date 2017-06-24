// AIDLServiceInterface.aidl
package com.example.clementvacandard.banqueimages;

// Declare any non-default types here with import statements

interface AIDLServiceInterface {

       int add(in int ValueFirst, in int valueSecond);

       Bitmap getImage(in int index);
}
