(ns clj-as-art.synth.laserbeam
  (:use [overtone.live]))

;; // cf. https://github.com/brunoruviaro/SynthDefs-for-Patterns/blob/master/laserbeam.scd
;; (
;; SynthDef("laserbeam", {
;;     arg out = 0, pan = 0.0, freq = 440, amp = 0.1, dur = 0.25;
;;     var snd, freqenv, ampenv;
;;     // frequency envelope
;; 	freqenv = Env.perc(releaseTime: dur/2).kr(doneAction: 2);
;; 	// amplitude envelope
;; 	ampenv = Env.perc(releaseTime: dur/2).kr(doneAction: 2);
;; 	// snd = LFTri.ar(freq: freq * freqenv, mul: ampenv);
;; 	// snd = Saw.ar(freq: freq * freqenv, mul: ampenv);
;; 	snd = LFSaw.ar(freq: freq * freqenv, mul: ampenv);
;; 	// snd = SinOsc.ar(freq: freq * freqenv, mul: ampenv);
;;     Out.ar(out, Pan2.ar(snd, pan));
;; }).add;
;; )
;;
;; (
;; Pbind(
;; 	\instrument, "laserbeam",
;; 	\pan, Pwhite(-1.0, 1.0),
;; 	\midinote, Pwhite(60, 140),
;; 	// \midinote, 150,
;; 	\amp, 0.25,
;; 	\dur, 0.125
;; 	// \dur, 1.0/Prand([4,4,4,4,8,16], inf)
;; ).play;
;; )
(definst laserbeam [pan 0.0 freq 440 amp 1.0 dur 0.25]
  (let [freqenv (env-gen (perc 0.000 (/ dur 2.0)) :action FREE)
        ampenv (env-gen (perc 0.000 (/ dur 2.0)) :action FREE)
        src (lf-tri [(* freq freqenv)])
        ;;src (* 0.6 (lf-saw [(* freq freqenv)]))
        ;;src (saw [(* freq freqenv)])
        ]
    (pan2 (* src ampenv amp) pan))
  )

;;(volume (/ 40 127))

;;(laserbeam :freq 300 :dur 2)
;;(laserbeam :freq 3000 :dur 1)
;;(laserbeam :freq 10000 :dur 0.25)

;;(stop)
;; (laserbeam)
;; (laserbeam :freq 500 :armp 0.2 :dur 1)

;; (inst-fx! laserbeam fx-distortion2)
;; (inst-fx! laserbeam fx-distortion-tubescreamer)
;; (inst-fx! laserbeam fx-chorus)
;; (clear-fx laserbeam)
