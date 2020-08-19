package com.example.androidtipcalculator

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val INITIALTIPPERCENT = 15  //initial % variable defined for initial tip percent
    private val INITIALTIPPEOPLE = 1   // initial people variable defined for initial number of people to split the bill


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBarTip.progress = INITIALTIPPERCENT   //setting the initial seek bar value for tip percent
        seekBarPeople.progress = INITIALTIPPEOPLE //setting th initial seek bar value for people to split the bill

        tipAmountDisplay.text = "$INITIALTIPPERCENT%"   //setting the text for initial % in front of seek bar

        billSplitPeople.text = "$INITIALTIPPEOPLE"      //setting the text for initial people to split bill in front of it's respective seek bar

        //changing the value of seek bar once the user modifies it on the screen for tip percentage
        seekBarPeople.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                billSplitPeople.text = "$progress"

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        //changing the value of seek bar once the user modifies it on the screen for number of people to split the bill
        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tipAmountDisplay.text = "$progress%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }

    //a mutable list defined to store the keys being pressed by the user
    val numberCache: MutableList<String> = arrayListOf()

    //funtion defined to join the list of texts for corresponding buttons being pressed
    fun makeString(list: List<String>,joiner: String = "") : String {
        if (list.isEmpty()) return ""
        return list.reduce { r, s -> r + joiner + s}
    }


    //function defined to update the display of the bill amount text whenever user presses a specific button on the keypad
    fun updateDisplay(mainDisplayString: String){

        val fullCalculationDisplayTextView  = findViewById(R.id.etBase) as TextView
        fullCalculationDisplayTextView.text = mainDisplayString
    }

    //function defined to store the keys pressed in a list and calling other two functions to update the display
    fun numberClick(view: View) {

        val button = view as Button
        val numberString = button.text;
        numberCache.add(numberString.toString())
        val text = makeString(numberCache)
        updateDisplay(text)
    }


    //function defined to clear the display once the clear key is pressed on the keypad
    fun clearClick(view: View){

        //calling the function to clear the mutable list
        clearCache()

        //sending an empty string to display function to clear the display for the user
        updateDisplay("")

        //clearing the other values
        tvTipAmount.text= ""
        tvTotalAmount.text= ""
        seekBarTip.progress = INITIALTIPPERCENT
        seekBarPeople.progress = INITIALTIPPEOPLE
    }

    //function defined to clear the number cache mutable list
    fun clearCache(){
        numberCache.clear()
    }

    //function defined to implement the backspace key on the keypad
    fun backSpace(view: View){

        if(etBase.text.isNotEmpty()) {
            var len = etBase.text.toString().length - 1
            var name: String = etBase.text.toString().substring(0, len)
            etBase.setText(name)
        }
    }

    //function defined to calcualate the tip and total amount
    fun calculateTip(view : View){
        if (etBase.text.isEmpty()){

            //checking if the user has pressed the calculate key without entering the bill amount
            //displaying the error message
            Toast.makeText(applicationContext, "Please enter the bill amount",Toast.LENGTH_SHORT).show()
            tvTipAmount.text= ""
            tvTotalAmount.text = ""
            return
        }

        //calculating the tip and total amount when the bill is not to be split
        if(seekBarPeople.progress == 1 || seekBarPeople.progress == 0) {
            val baseAmount = etBase.text.toString().toDouble()
            val tipAmount = baseAmount * seekBarTip.progress / 100
            tvTipAmount.text = "%.2f".format(tipAmount)
            tvTotalAmount.text = "%.2f".format(baseAmount + tipAmount)
        }

        //calculating the tip and total amount when the bill has to be split
        else{
            val baseAmount = etBase.text.toString().toDouble()
            val tipAmount = ( baseAmount * seekBarTip.progress / 100 ) / seekBarPeople.progress
            tvTipAmount.text = "%.2f".format(tipAmount) + " per person "
            tvTotalAmount.text = "%.2f".format((baseAmount / seekBarPeople.progress) + tipAmount) + " per person "
        }
    }
}
