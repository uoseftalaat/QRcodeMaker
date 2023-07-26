package com.example.qrcode.ui

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.example.qrcode.R
import com.example.qrcode.databinding.FragmentQrScannerBinding


class QrScannerFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var binding:FragmentQrScannerBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentQrScannerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val scannerView = binding.codeScanner
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                val alertDialog = AlertDialog.Builder(context)
                    .setTitle("link opening")
                    .setMessage("do you want to open this link < ${it.text} >")
                    .setPositiveButton("Yes") { _, _ ->
                        val uri = Uri.parse(it.text)
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        }catch (e:Exception){
                            Toast.makeText(context,"sorry this is not a valid link",Toast.LENGTH_LONG).show()
                            codeScanner.startPreview()
                        }
                    }
                    .setNegativeButton("No") { _, _ ->
                        codeScanner.startPreview()
                    }
                    .create().show()


            }
        }
        binding.buttonToGeneration.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.action_qrScannerFragment_to_qrGenneratorFragment)
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

}