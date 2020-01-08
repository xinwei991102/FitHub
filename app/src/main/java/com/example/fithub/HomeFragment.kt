package com.example.fithub

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.CompactCalendarView.CompactCalendarViewListener
import com.github.sundeepk.compactcalendarview.domain.Event
import kotlinx.android.synthetic.main.fragment_home.*
import java.time.LocalDateTime
import java.util.*

class HomeFragment:Fragment() {
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
        val dateFormatMonth = SimpleDateFormat("Mon yyyy", Locale.getDefault())
        val dateFormatCompare = SimpleDateFormat("YYMMDD", Locale.getDefault())

        compactCalendar.setUseThreeLetterAbbreviation(true)
        val c = GregorianCalendar()
        c.set(Calendar.HOUR_OF_DAY, 0) //anything 0 - 23
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        val currentTimeInLong = c.time.time

        val event1 = Event(Color.BLUE, 1578537000000L, "test")
        compactCalendar.addEvent(event1)

        //set current event //milliseconds in one week-604800000L
        val event2 = Event(Color.RED, currentTimeInLong, "test2")
        compactCalendar.addEvent(event2)

        compactCalendar.setListener(object : CompactCalendarViewListener{
            override fun onDayClick(dateClicked: Date?) {
                //val dateClickedFormatted = dateFormatCompare.format(dateClicked)
                //val eventTimeInString= dateFormatCompare.format(event2.timeInMillis)
                if(dateClicked?.time == event2.timeInMillis){
                    Toast.makeText(context, event2.data.toString(), Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "No event", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                textViewMonth.text = dateFormatMonth.format(firstDayOfNewMonth)
            }
        })
    }
}
