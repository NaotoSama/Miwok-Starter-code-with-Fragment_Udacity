package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.miwokWithFragment.R;

import java.util.ArrayList;

/**
 * 這個Classs目的是要設置ArrayList，並透過MediaPlayer、AudioManager、OnAudioFocusChangeListener、releaseMediaPlayer
 * 設置音檔到ArrayList
 */


/**
 * A simple {@link Fragment} subclass.
 */
public class ColorsFragment extends Fragment {

    /**
     * Create a global MediaPlayer variable to be called below.
     * Handles playback of all the sound files
     */
    private MediaPlayer mMediaPlayer;   // Create a global media player variable to be called below.


    /**
     * Create a global AudioManager variable to be called below.
     * Handles audio focus when playing a sound file
     */
    private AudioManager mAudioManager;


    /**
     * Create an OnCompletionListener variable
     * This listener gets triggered when the {@link MediaPlayer} has completed playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };


    /**
     * Create OnAudioFocusChangeListener via android's built-in AudioManager class
     * This listener gets triggered whenever the audio focus changes (i.e., we gain or lose audio focus because of another app or device).
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (mMediaPlayer == null)
                return;
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||                       //  「||」的意思是「或」
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {                            // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start();                                                                //Resume Playback.
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {                            // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                releaseMediaPlayer();                                                                // Stop playback and clean up resources
            }
        }
    };


    public ColorsFragment() {
        // Required empty public constructor
        //為什麼在每個片段中都需要一個公共的空構造函數？
        //片段的所有子類都必須包括一個公共的無參數的構造函數。框架通常會在需要時重新實例化一個片段類，特別是在狀態恢復期間，
        //並且需要能夠找到這個構造器來實例化它。如果無參數構造函數不可用，則在狀態恢復期間會發生運行時異常。
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list_view, container, false);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);  //Initialize the AudioManager to request audio focus 当我们希望获取到系统服务时，可以调用Context的getSystemService方法
                                                                                                  //Fragment does not have access to system services, whereas the Activity does, so get the Activity object instance first. This is the Activity that encloses the current Fragment, which will be the NumbersActivity for the NumbersFragment. Then call getSystemService(String) on that Activity object.

        // [Best method for creating a list of words]
        // Create a ArrayList of words
        final ArrayList<Word> words = new ArrayList<Word> ();
        words.add(new Word("red 紅","weṭeṭṭi", R.drawable.color_red, R.raw.color_red));     // 語法精簡自 Word w = new Word("one","lutti"); words.add(w);
        words.add(new Word("green 綠", "chokokki", R.drawable.color_green, R.raw.color_green));
        words.add(new Word("brown 咖啡", "ṭakaakki", R.drawable.color_brown, R.raw.color_brown));
        words.add(new Word("gray 灰", "ṭopoppi", R.drawable.color_gray, R.raw.color_gray));
        words.add(new Word("black 黑", "kululli", R.drawable.color_black, R.raw.color_black));
        words.add(new Word("white 白", "kelelli", R.drawable.color_white, R.raw.color_white));
        words.add(new Word("dusty yellow 濁黃", "ṭopiisә", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        words.add(new Word("mustard yellow 綠黃", "chiwiiṭә", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));


        // // // [Best method using an Array Adapter to couple with the ListView in xml for view recycling and reduced memory usage]
        // Create an {@link ArrayAdapter}, whose data source is a list of Strings. The adapter knows how to create layouts for each item in the list,
        // using the simple_list_item_1.xml layout resource defined in the Android framework. This list item layout contains a single {@link TextView},
        // which the adapter will set to display a single word. ("this" means the NumbersActivity. "simple_list_item_1" means android's pre-defined default layout file to show ListView. "words" is the ArrayList.)
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_colors);          // There’s a problem with the arguments passed into the WordAdapter constructor because the first parameter “this” refers to this class (which is the NumbersFragment), and a Fragment is not a valid Context.
                                                                                                     // However, the code used to work when “this” referred the NumbersActivity because an Activity is a valid Context. Fix the error by passing in a reference to the Activity ""getActivity()"" that encloses this Fragment as the context.


        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}. There should be a {@link ListView} with the view ID called list,
        // which is declared in the activity_numbers.xml layout file.
        ListView listView = (ListView) rootView.findViewById(R.id.word_list_view);           //Fragment does not have a findViewById method, whereas the Activity did have that method, so call findViewById on the rootView object, which should contain children views such as the ListView

        // Make the {@link ListView} use the {@link ArrayAdapter} we created above, so that the {@link ListView} will display list items for each word in the list of words.
        // Do this by calling the setAdapter method on the {@link ListView} object and pass in 1 argument, which is the {@link ArrayAdapter} with the variable name itemsAdapter.
        listView.setAdapter(adapter);


        /**
         * Set an OnItemClickListener onto the list view.
         **/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {                                            //setOnItemClickListener is under the AdapterView Class.
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getActivity(), "Pronouncing 發音中", Toast.LENGTH_SHORT).show(); //Show a toast message when the word_list_view is tapped. //The code used to work when “this” referred the NumbersActivity because an Activity is a valid Context. Fix the error by passing in a reference to the Activity ""getActivity()"" that encloses this Fragment as the context.

                Word word =words.get(position);                                                                            // Get the {@link Word} object at the given position the user clicked on

                releaseMediaPlayer();                                                                                      // Release the media player if it currently exists because we are about to // play a different sound file


                /**在手指按到物件的時候就要 request audio focus。
                 *在系統抓取到每個word的位置MediaPlayer準備播放不同的音檔時 request audio focus
                 * 設置一個integer變數叫 result並對AudioManager要求AudioFocus，若result等於取得了AudioFocus，則創建並播放MediaPlayer，並對MediaPlayer設置OnCompletionListener，其內容已在最上方的CompletionListener變數中設定過
                 * 屬性之所以會是int是因為所要求的AudioFocus值都是字母全大寫的常數(例如AUDIOFOCUS_GAIN_TRANSIENT)，常數也是一種integer
                 **/
                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,      //Pass in the AudioFocusChangeListener
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,                                         //Pass in the audio stream type
                        // Request short-period focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);                         //Pass in audio focus type

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {                                                // 若取得AudioFocus後，可創建MediaPlayer、開始播放並設置OnCompletionListener。We have audio focus now (granted). Start playback through the following code.
                    mMediaPlayer = MediaPlayer.create(getActivity(), word.getmAudioResourceId());               // Create and setup the {@link MediaPlayer} for the audio resource associated with the current word
                    mMediaPlayer.start();                                                                                      // Start the audio file
                    //  叫MediaPlayer透過"word" variable去抓聲音檔( word.getmAudioResourceId() )，放在NumbersActivity裡
                    //  最後叫MediaPlayer播放聲音檔


                    // Setup the OnCompletionListener on the media player, so that we can stop and release the media player once the sound has finished playing.
                    // Pass in the global variable mCompletionListener
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

        return rootView;
    }


    /**
     * When the activity is stopped, release the media player resources because we won't be playing any more sounds.
     * 讓App在使用者跳離畫面時釋放播放器，不繼續播放音檔
     */
    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }


    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
        }
    }


}
