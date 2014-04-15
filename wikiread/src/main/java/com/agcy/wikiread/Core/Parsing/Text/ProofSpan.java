package com.agcy.wikiread.Core.Parsing.Text;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.style.ClickableSpan;
import android.text.style.UpdateAppearance;
import android.view.View;

/**
 * Created by kiolt_000 on 20.03.14.
 */
public class ProofSpan extends ClickableSpan
        implements UpdateAppearance, ParcelableSpan {

    @Override
    public void onClick(View widget) {

    }

    @Override
    public int getSpanTypeId() {
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
