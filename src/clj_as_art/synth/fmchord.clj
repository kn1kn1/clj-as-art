(ns clj-as-art.synth.fmchord
  (:use [overtone.live]))

;; SynthDef (\fmchord01, {|freq=440, amp=0.2, dur=2|
;;                        var index_env, amp_env, out;
;;                        index_env = EnvGen.ar (Env.perc (0.0001, 0.2, 1, -4));
;;                        amp_env = EnvGen.ar (Env.perc (0.0001, 1, dur, -4), doneAction:2);
;;                        out = PMOsc.ar (freq, freq * 1.02, (index_env * 2)) * amp_env;
;;                        out = FreeVerb.ar (out.dup, 0.5, 0.8, 0.9);
;;                        Out.ar (0, out * amp);
;;                        }).add;
(definst fmchord01 [freq 440 amp 0.2 dur 2]
  (let [index-env (env-gen (perc 0.0001 0.2 1 -4))
        amp-env (env-gen (perc 0.0001 dur 1 -4) :action FREE)
        snd (pm-osc freq (* freq 1.02) (* index-env 2))
        reverb (free-verb (* snd amp-env) 0.5 0.8 0.9)]
    (pan2 (* amp reverb))))
;;(fmchord01)
