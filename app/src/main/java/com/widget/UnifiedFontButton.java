// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class UnifiedFontButton extends Button {

    public UnifiedFontButton(Context context) {
        super(context);
        init();
    }

    public UnifiedFontButton(Context context, AttributeSet attributeset) {
        super(context, attributeset);
        init();
    }

    public UnifiedFontButton(Context context, AttributeSet attributeset, int i) {
        super(context, attributeset, i);
        init();
    }

    private void init() {
        int i;
        Typeface typeface = null;
        if (getTypeface() != null) {
            i = getTypeface().getStyle();
        } else {
            i = 0;
        }
        if (typeface != null) {
            setTypeface(typeface);
        }
    }
}
