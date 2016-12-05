https://github.com/pnikosis/materialish-progress

Usage

You can create your own progress wheel in xml like this (remeber to add xmlns:wheel="http://schemas.android.com/apk/res-auto"):

<com.pnikosis.materialishprogress.ProgressWheel
        android:id="@+id/progress_wheel"
        android:layout_width="80dp"
        android:layout_height="80dp"
        android:layout_centerHorizontal="true"
        android:layout_centerVertical="true"
        wheel:matProg_barColor="#5588FF"
        />


progressWheel.spin();开始转
progressWheel.stopSpinning();停止转


