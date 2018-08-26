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
 * A simple {@link Fragment} subclass.
 */
public class PhrasesFragment extends Fragment {

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


    public PhrasesFragment() {
        // Required empty public constructor
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
        words.add(new Word("Where are you going? 你去哪?","minto wuksus", R.raw.phrase_where_are_you_going));     // 語法精簡自 Word w = new Word("one","lutti"); words.add(w);
        words.add(new Word("What is your name? 你叫什名字?", "tinnә oyaase'nә", R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is... ", "oyaaset...", R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling? 你感覺怎麼樣?", "michәksәs?", R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I’m feeling good. 我覺得很好", "kuchi achit", R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming? 你要來嗎?", "әәnәs'aa?", R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I’m coming. 我會來", "hәә’ әәnәm", R.raw.phrase_yes_im_coming));
        words.add(new Word("I’m coming. 我要來囉", "әәnәm", R.raw.phrase_im_coming));
        words.add(new Word("Let’s go. 走吧", "yoowutis", R.raw.phrase_lets_go));
        words.add(new Word("Come here. 過來這兒", "әnni'nem", R.raw.phrase_come_here));


        // // // [Best method using an Array Adapter to couple with the ListView in xml for view recycling and reduced memory usage]
        // Create an {@link ArrayAdapter}, whose data source is a list of Strings. The adapter knows how to create layouts for each item in the list,
        // using the simple_list_item_1.xml layout resource defined in the Android framework. This list item layout contains a single {@link TextView},
        // which the adapter will set to display a single word. ("this" means the NumbersActivity. "simple_list_item_1" means android's pre-defined default layout file to show ListView. "words" is the ArrayList.)
        WordAdapter adapter = new WordAdapter((getActivity()), words, R.color.category_phrases);         // There’s a problem with the arguments passed into the WordAdapter constructor because the first parameter “this” refers to this class (which is the NumbersFragment), and a Fragment is not a valid Context.
                                                                                                      // However, the code used to work when “this” referred the NumbersActivity because an Activity is a valid Context. Fix the error by passing in a reference to the Activity ""getActivity()"" that encloses this Fragment as the context.

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}. There should be a {@link ListView} with the view ID called list,
        // which is declared in the activity_numbers.xml layout file.
        ListView listView = (ListView) rootView.findViewById(R.id.word_list_view);                     //Fragment does not have a findViewById method, whereas the Activity did have that method, so call findViewById on the rootView object, which should contain children views such as the ListView

        // Make the {@link ListView} use the {@link ArrayAdapter} we created above, so that the {@link ListView} will display list items for each word in the list of words.
        // Do this by calling the setAdapter method on the {@link ListView} object and pass in 1 argument, which is the {@link ArrayAdapter} with the variable name itemsAdapter.
        listView.setAdapter(adapter);


        /**
         * Set an OnItemClickListener onto the list view.
         **/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {                                            //setOnItemClickListener is under the AdapterView Class.
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText((getActivity()), "Pronouncing 發音中", Toast.LENGTH_SHORT).show(); //Show a toast message when the word_list_view is tapped.   //The code used to work when “this” referred the NumbersActivity because an Activity is a valid Context. Fix the error by passing in a reference to the Activity ""getActivity()"" that encloses this Fragment as the context.

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
                    mMediaPlayer = MediaPlayer.create(getActivity(), word.getmAudioResourceId());               // Create and setup the {@link MediaPlayer} for the audio resource associated with the current word.   //The code used to work when “this” referred the NumbersActivity because an Activity is a valid Context. Fix the error by passing in a reference to the Activity ""getActivity()"" that encloses this Fragment as the context.
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
     * You can't copy the exact onStop() method over from the NumbersActivity because the Fragment onStop() method has a slightly different method signature.
     * In case you’re wondering, the Activity class uses the “protected” modifier on the method, while the Fragment class has the “public” modifier on the method.
     */
    @Override
    public void onStop() {
        super.onStop();

        // When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
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

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            // 確定不再播放音檔時，或是用戶關閉App時abandon AudioFocus
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

}
