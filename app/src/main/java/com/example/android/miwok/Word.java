package com.example.android.miwok;

/** Define a customized class named "word", create Strings, and generate constructor and getter by right clicking the mouse and select "Generate"*/

import android.widget.ImageView;

/**
* {@link Word} represents a vocabulary word that the user wants to learn.
* It contains a default translation, a Miwok translation, and an image  for that word.
 *此 Word Class中的variables好比一個個模具，等者被放進constructor中排序
*/
public class Word {
    /** Default translation for the word */
    private String mDefaultTranslation;

    /** Miwok translation for the word */
    private String mMiwokTranslation;

    /** Image resource ID for the word
     * Set  the image variable to the "no image" state by default
     * */
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    /** Audio resource ID for the word
     * */
    private int mAudioResourceId;

    /**
     * Create a constant that indicates a "no image" state.
     * Final makes it constant. Once you declare final, you can not re-assign any value to it later.
     * The name of a constant should be capitalized. Each word should be separated by an underscore.
     * "0" also means false in if/else conditions. ("1"  means true.)
     * */
    //The combination of final and static gives you the ability to create constants.
    //A static variable (also called, class variable) is not specific to any instance. It is shared among all the instances of that class.
    //Static variables are loaded into memory when the class is first time loaded. That's why it is shared by all the instances.
    private static final int NO_IMAGE_PROVIDED = 0;


    /**
     * Call the constructor to create a new Word object. The object name has to be identical to the Class name
     * @param defaultTranslation is the word in a language that the user is already familiar with  (such as English)
     * @param miwokTranslation is the word in the Miwok language
     * This first constructor will be used by PhrasesActivity.java
     *  Constructor負責把模具排序
     */
    //Call the constructor method to initialize the global member variables at the top based on the values we pass into the constructor.
    public Word(String defaultTranslation, String miwokTranslation, int audioResourceId) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mAudioResourceId = audioResourceId;
    }

    /**
     *  @param imageResourceId is the drawable resource ID for the image associated with the word
     *  This second constructor will be used by NumbersActivity, ColorsActivity, and FamilyActivity.java
     *   Constructor負責把模具排序
     * */
    //Call the constructor method to initialize the global member variables at the top based on the values we pass into the constructor.
    public Word(String defaultTranslation, String miwokTranslation, int imageResourceId, int audioResourceId) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mImageResourceId = imageResourceId;
        mAudioResourceId = audioResourceId;
    }


    //The global member variables at the top are private, so we need to create public Getter methods for other classes to access them.
    /**
      * Call the getter method to get the default translation of the word.
     * Getter負責把各模具對應的材料抓來放進constructor
     */
    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }


    /**
      * Call the getter method to get the Miwok translation of the word.
      */
    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }

    /**
     * Call the getter method to get the image resource ID of the word.
     */
    public int getmImageResourceId() {
        return mImageResourceId;
    }

    /**
     * Call the getter method to get the audio resource ID of the word.
     */
    public int getmAudioResourceId() {
        return mAudioResourceId;
    }

    /**
     * Use boolean to determine whether the word at the current position has an image.
     *    !=   means "not equal to"
     *  新建一個 method 判斷是否有圖片
     *  當圖片 ID 不等於 0 時，返回值 true，表示有圖片
     *  當圖片 ID 等於 0 時，返回值 false，表示無圖片
     */
    public boolean hasImage () {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }


}