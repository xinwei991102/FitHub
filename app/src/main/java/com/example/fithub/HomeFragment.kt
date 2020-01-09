package com.example.fithub

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.CompactCalendarView.CompactCalendarViewListener
import com.github.sundeepk.compactcalendarview.EventsContainer
import com.github.sundeepk.compactcalendarview.domain.Event
import kotlinx.android.synthetic.main.fragment_home.*
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

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
        val dateFormatMonth = SimpleDateFormat("MM yyyy", Locale.getDefault())

        compactCalendar.setUseThreeLetterAbbreviation(true)

        val eventColor = arrayOf(Color.BLUE, Color.RED, Color.CYAN, Color.GREEN)
        val eventData = arrayOf("Aerobic Exercise", "Core Exercise", "Arm Exercise", "Leg Exercise", "No record...")

        //set date into 12mn in term milliseconds
        val c = GregorianCalendar()
        c.set(Calendar.HOUR_OF_DAY, 0) //anything 0 - 23
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        val currentTimeInLong = c.time.time

        textViewMonth.text = dateFormatMonth.format(c.time)

        //set event
        var events = ArrayList<Event>()
        var event:Event
        val sharedPref: SharedPreferences

        val workout = 0

        //TODO - loop to add more event
        event = Event(eventColor[workout], currentTimeInLong - 86400000L, eventData[workout])
        events.add(event)
        event = Event(eventColor[1], currentTimeInLong - 86400000L - 86400000L, eventData[1])
        events.add(event)
        event = Event(eventColor[2], currentTimeInLong - 86400000L - 86400000L - 86400000L, eventData[2])
        events.add(event)
        event = Event(eventColor[2], currentTimeInLong - 86400000L - 86400000L - 86400000L, eventData[2])
        events.add(event)
        event = Event(eventColor[3], currentTimeInLong - 86400000L - 86400000L - 86400000L, eventData[3])
        events.add(event)

        compactCalendar.addEvents(events)
        compactCalendar.setListener(object : CompactCalendarViewListener{
            override fun onDayClick(dateClicked: Date?) {

                var eventString = eventData[4]
                if (compactCalendar.getEvents(dateClicked?.time!!).isNotEmpty()) {
                    eventString = "Congratulation! You done the: \n"
                    for(event in compactCalendar.getEvents(dateClicked.time)){
                        eventString += event.data.toString() + "\n"
                    }
                }else if(dateClicked.time == currentTimeInLong){
                    eventString += "\nLet's start do an activity now!"
                }
                textViewEvent.text = eventString

            }
            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                textViewMonth.text = dateFormatMonth.format(firstDayOfNewMonth)
            }
        })
    }
}
