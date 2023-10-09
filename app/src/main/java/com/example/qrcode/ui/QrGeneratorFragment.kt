package com.example.qrcode.ui


import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidmads.library.qrgenearator.QRGSaver
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.qrcode.R
import com.example.qrcode.databinding.FragmentQrGenneratorBinding
import com.example.qrcode.others.Constants.IMAGE_SET_VALUE
import com.google.zxing.WriterException


class QrGeneratorFragment : Fragment() {
    lateinit var binding: FragmentQrGenneratorBinding
    lateinit var bitmap: Bitmap
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQrGenneratorBinding.inflate(inflater,container,false)
        binding.buttonGenerate.setOnClickListener{
            if(binding.inputvalue.text.toString().isNotEmpty()){
                createQR()
            }
            else{
                Toast.makeText(context,"please enter valid text",Toast.LENGTH_SHORT).show()
            }
        }
        binding.buttonToScan.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.action_qrGenneratorFragment_to_qrScannerFragment)
        }
        binding.saveImage.setOnClickListener{
            if(binding.imageView.tag == IMAGE_SET_VALUE){
                saveImage()
            }
            else{
                Toast.makeText(context,"you didn't do any qr yet",Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    private fun createQR(){
        val qrgEncoder = QRGEncoder(binding.inputvalue.text.toString(), null, QRGContents.Type.TEXT, 1000)
        qrgEncoder.colorBlack = Color.BLACK
        qrgEncoder.colorWhite = Color.WHITE
        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.getBitmap(0)
            // Setting Bitmap to ImageView
            binding.imageView.setImageBitmap(bitmap)
            binding.imageView.tag = IMAGE_SET_VALUE

        } catch (e: WriterException) {
            Log.v(TAG, e.toString())
        }
    }
    private fun saveImage(){
        val qrgSaver =  QRGSaver()
        val savepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/QRCode/";
        qrgSaver.save(savepath, binding.inputvalue.getText().toString().trim(), bitmap, QRGContents.ImageType.IMAGE_JPEG)
        Toast.makeText(context,"saved",Toast.LENGTH_LONG).show()
    }

    // Save with location, value, bitmap returned and type of Image(JPG/PNG).
    //QRGSaver qrgSaver = new QRGSaver();
    //qrgSaver.save(savePath, edtValue.getText().toString().trim(), bitmap, QRGContents.ImageType.IMAGE_JPEG);
}