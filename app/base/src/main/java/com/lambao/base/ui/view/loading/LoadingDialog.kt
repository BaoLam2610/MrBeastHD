package com.lambao.base.ui.view.loading

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.graphics.drawable.toDrawable
import com.lambao.base.databinding.LayoutLoadingDialogBinding

class LoadingDialog(context: Context) : Dialog(context) {
    lateinit var binding: LayoutLoadingDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutLoadingDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        setCancelable(false)
        window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
    }
}