package com.matt.flashcards;

import android.content.Context;
import android.widget.Toast;

public class DebugToast {
    public DebugToast(Context context, CharSequence charSequence) {
        Toast.makeText(context, charSequence, Toast.LENGTH_SHORT).show();
    }
}
