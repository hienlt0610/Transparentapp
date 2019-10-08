package dev.hienlt.transparentapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class PhoneInputDialogFragment extends DialogFragment implements View.OnClickListener {
    private static final String TAG = PhoneInputDialogFragment.class.getSimpleName();
    private LinearLayout llPinEntryBar;

    public static void show(FragmentManager fragmentManager) {
        Fragment fragment = fragmentManager.findFragmentByTag(TAG);
        if (fragment instanceof PhoneInputDialogFragment) {
            ((PhoneInputDialogFragment) fragment).dismiss();
        }
        PhoneInputDialogFragment dialogFragment = new PhoneInputDialogFragment();
        dialogFragment.show(fragmentManager, TAG);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_phone, container, false);
        setCancelable(false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initClickListener(view);
        llPinEntryBar = view.findViewById(R.id.ll_pin_entry_bar);
        initPinEntry();
    }

    private void initClickListener(View view) {
        view.findViewById(R.id.btn_submit).setOnClickListener(this);
        view.findViewById(R.id.btn_number_0).setOnClickListener(this);
        view.findViewById(R.id.btn_number_1).setOnClickListener(this);
        view.findViewById(R.id.btn_number_2).setOnClickListener(this);
        view.findViewById(R.id.btn_number_3).setOnClickListener(this);
        view.findViewById(R.id.btn_number_4).setOnClickListener(this);
        view.findViewById(R.id.btn_number_5).setOnClickListener(this);
        view.findViewById(R.id.btn_number_6).setOnClickListener(this);
        view.findViewById(R.id.btn_number_7).setOnClickListener(this);
        view.findViewById(R.id.btn_number_8).setOnClickListener(this);
        view.findViewById(R.id.btn_number_9).setOnClickListener(this);
        view.findViewById(R.id.btn_clear).setOnClickListener(this);
    }

    private void initPinEntry() {
        int pinEntrySize = getResources().getDimensionPixelSize(R.dimen.pin_entry_size);
        int pinEntrySpacing = getResources().getDimensionPixelSize(R.dimen.pin_entry_spacing);
        int totalEntry = 10;
        for (int i = 0; i < totalEntry; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pinEntrySize, pinEntrySize);
            if (i < totalEntry - 1) {
                params.rightMargin = pinEntrySpacing;
            }
            TextView entryTextView = createTextViewEntry();
            entryTextView.setOnClickListener(entryClick);
            llPinEntryBar.addView(entryTextView, params);
        }
        llPinEntryBar.getChildAt(0).setSelected(true);
    }

    private int getSelectedEntry() {
        for (int i = 0; i < llPinEntryBar.getChildCount(); i++) {
            if (llPinEntryBar.getChildAt(i).isSelected()) {
                return i;
            }
        }
        return 0;
    }

    private void setSelectedEntry(int position) {
        if (position >= 0 && position < llPinEntryBar.getChildCount()) {
            resetEntryState();
            llPinEntryBar.getChildAt(position).setSelected(true);
        }
    }

    private void setSelectedEntry(View view) {
        setSelectedEntry(llPinEntryBar.indexOfChild(view));
    }

    private View.OnClickListener entryClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setSelectedEntry(v);
        }
    };

    private void resetEntryState() {
        for (int i = 0; i < llPinEntryBar.getChildCount(); i++) {
            View childAt = llPinEntryBar.getChildAt(i);
            childAt.setSelected(false);
        }
    }

    private TextView createTextViewEntry() {
        TextView textView = new TextView(getActivity());
        textView.setBackgroundResource(R.drawable.bg_pin);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(18);
        textView.setSelected(false);
        return textView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null || getDialog().getWindow() == null) return;
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                dismiss();
                break;
            case R.id.btn_clear:
                setEntryValue(null);
                prevEntry();
                break;
            default:
                String value = (String) v.getTag();
                setEntryValue(value);
                nextEntry();
                break;
        }
    }

    private void setEntryValue(String value) {
        int selectedEntry = getSelectedEntry();
        TextView entry = (TextView) llPinEntryBar.getChildAt(selectedEntry);
        entry.setText(value);
    }

    private void nextEntry() {
        int selectedEntry = getSelectedEntry();
        if (selectedEntry < llPinEntryBar.getChildCount() - 1) {
            setSelectedEntry(selectedEntry + 1);
        }
    }

    private void prevEntry() {
        int selectedEntry = getSelectedEntry();
        if (selectedEntry > 0) {
            setSelectedEntry(selectedEntry - 1);
        }
    }
}
