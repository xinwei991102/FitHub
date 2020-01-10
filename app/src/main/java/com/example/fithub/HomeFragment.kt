package com.example.fithub

import android.annotation.TargetApi
import android.content.SharedPreferences
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.CompactCalendarView.CompactCalendarViewListener
import com.github.sundeepk.compactcalendarview.domain.Event
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.collections.ArrayList
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BulletSpan


class HomeFragment:Fragment() {
    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var mSSBuilder:SpannableStringBuilder
    var eventString = "No date selected"

    //array
    private val eventColor = arrayOf(Color.BLUE, Color.RED, Color.CYAN, Color.GREEN)
    //private val eventData = arrayOf("Aerobic Exercise", "Core Exercise", "Arm Exercise", "Leg Exercise")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

    return inflater.inflate(R.layout.fragment_home, container, false)}

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val compactCalendar: CompactCalendarView = view?.findViewById(R.id.calendarViewCalendar)!!
        val dateFormatMonth = SimpleDateFormat("MM yyyy", Locale.getDefault())

        compactCalendar.setUseThreeLetterAbbreviation(true)

        //set date into 12mn in term milliseconds
        val c = GregorianCalendar()
        c.set(Calendar.HOUR_OF_DAY, 0) //anything 0 - 23
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        val currentTimeInLong = c.time.time

        textViewMonth.text = dateFormatMonth.format(c.time)

        //set event
        val events = ArrayList<Event>()
        val event:Event
        val user = FirebaseAuth.getInstance().currentUser
        pref = requireContext().getSharedPreferences(user?.uid, 0) // 0 - for private mode
        var workout = 0
        editor = pref.edit()
        var eventData =""
        if(pref.getString("completed_exercise_name", "").isNotEmpty()){
            when (pref.getString("completed_exercise_name", "")) {
                getString(R.string.aerobic_exercise) -> {
                    workout = 0  //0 aerobic exercise
                    eventData = getString(R.string.aerobic_exercise)
                }
                getString(R.string.core_exercise) -> {
                    workout = 1  //1 core exercise
                    eventData = getString(R.string.core_exercise)
                }
                getString(R.string.arm_exercise) -> {
                    workout = 2 //2 arm exercise
                    eventData = getString(R.string.arm_exercise)
                }
                getString(R.string.leg_exercise) -> {
                    workout = 3  //3 leg exercise
                    eventData = getString(R.string.leg_exercise)
                }
                getString(R.string.testing_exercise) -> {
                    eventData = getString(R.string.testing_exercise)
                }

            }
            event = Event(eventColor[workout], currentTimeInLong, eventData)
            events.add(event)
            editor.remove("completed_exercise_name")
            editor.apply()
        }

        compactCalendar.addEvents(events)

        //assign text into mSSBuilder
        mSSBuilder = SpannableStringBuilder(eventString)
        // Display the spannable text to TextView
        textViewEvent.text = mSSBuilder // text view display no record...

        compactCalendar.setListener(object : CompactCalendarViewListener{
            override fun onDayClick(dateClicked: Date?) {
                eventString = "No record..."
                if (compactCalendar.getEvents(dateClicked?.time!!).isNotEmpty()) {
                    eventString = "Congratulations! You done the: \n"
                    for(event in compactCalendar.getEvents(dateClicked.time)){
                        eventString += event.data.toString() + "\n"
                    }
                }else if(dateClicked.time == currentTimeInLong){
                    eventString += "\nLet's start do an activity now!"
                }
                mSSBuilder = SpannableStringBuilder(eventString)
                for(event in compactCalendar.getEvents(dateClicked.time)){
                    when (event.data.toString()) {
                        "Aerobic Exercise" -> {
                            showBullet("Aerobic Exercise")  // Generate bulleted list
                        }
                        "Core Exercise" -> {
                            showBullet("Core Exercise")
                        }
                        "Arm Exercise" -> {
                            showBullet("Arm Exercise")
                        }
                        "Leg Exercise" -> {
                            showBullet("Leg Exercise")
                        }
                    }
                }
                textViewEvent.text = mSSBuilder //eventString
            }
            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                textViewMonth.text = dateFormatMonth.format(firstDayOfNewMonth)
                textViewEvent.text = null
            }
        })
    }

    // Custom method to generate a bulleted list
    private fun showBullet(textToBullet:String ){
        // Initialize a new BulletSpan
        var bulletSpan = BulletSpan(0,Color.TRANSPARENT)
        when (textToBullet) {
            "Aerobic Exercise" -> {
                bulletSpan = BulletSpan(50, eventColor[0])
            }
            "Core Exercise" -> {
                bulletSpan = BulletSpan(50, eventColor[1])
            }
            "Arm Exercise" -> {
                bulletSpan = BulletSpan(50, eventColor[2])
            }
            "Leg Exercise" -> {
                bulletSpan = BulletSpan(50, eventColor[3])
            }
        }

        // Apply the bullet to the span
        mSSBuilder.setSpan(
            bulletSpan, // Span to add
            eventString.indexOf(textToBullet), // Start of the span (inclusive)
            eventString.indexOf(textToBullet) + 1,  // End of the span (exclusive)
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extend the span when text add later
        )
    }
}
