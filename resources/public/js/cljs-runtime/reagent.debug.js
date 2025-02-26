goog.provide('reagent.debug');
goog.require('cljs.core');
reagent.debug.has_console = (typeof console !== 'undefined');
reagent.debug.tracking = false;
if((typeof reagent !== 'undefined') && (typeof reagent.debug !== 'undefined') && (typeof reagent.debug.warnings !== 'undefined')){
} else {
reagent.debug.warnings = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof reagent !== 'undefined') && (typeof reagent.debug !== 'undefined') && (typeof reagent.debug.track_console !== 'undefined')){
} else {
reagent.debug.track_console = (function (){var o = ({});
o.warn = ((function (o){
return (function() { 
var G__3077__delegate = function (args){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(reagent.debug.warnings,cljs.core.update_in,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"warn","warn",-436710552)], null),cljs.core.conj,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.str,args)], 0));
};
var G__3077 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__3078__i = 0, G__3078__a = new Array(arguments.length -  0);
while (G__3078__i < G__3078__a.length) {G__3078__a[G__3078__i] = arguments[G__3078__i + 0]; ++G__3078__i;}
  args = new cljs.core.IndexedSeq(G__3078__a,0,null);
} 
return G__3077__delegate.call(this,args);};
G__3077.cljs$lang$maxFixedArity = 0;
G__3077.cljs$lang$applyTo = (function (arglist__3080){
var args = cljs.core.seq(arglist__3080);
return G__3077__delegate(args);
});
G__3077.cljs$core$IFn$_invoke$arity$variadic = G__3077__delegate;
return G__3077;
})()
;})(o))
;

o.error = ((function (o){
return (function() { 
var G__3081__delegate = function (args){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(reagent.debug.warnings,cljs.core.update_in,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"error","error",-978969032)], null),cljs.core.conj,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.str,args)], 0));
};
var G__3081 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__3082__i = 0, G__3082__a = new Array(arguments.length -  0);
while (G__3082__i < G__3082__a.length) {G__3082__a[G__3082__i] = arguments[G__3082__i + 0]; ++G__3082__i;}
  args = new cljs.core.IndexedSeq(G__3082__a,0,null);
} 
return G__3081__delegate.call(this,args);};
G__3081.cljs$lang$maxFixedArity = 0;
G__3081.cljs$lang$applyTo = (function (arglist__3083){
var args = cljs.core.seq(arglist__3083);
return G__3081__delegate(args);
});
G__3081.cljs$core$IFn$_invoke$arity$variadic = G__3081__delegate;
return G__3081;
})()
;})(o))
;

return o;
})();
}
reagent.debug.track_warnings = (function reagent$debug$track_warnings(f){
reagent.debug.tracking = true;

cljs.core.reset_BANG_(reagent.debug.warnings,null);

(f.cljs$core$IFn$_invoke$arity$0 ? f.cljs$core$IFn$_invoke$arity$0() : f.call(null));

var warns = cljs.core.deref(reagent.debug.warnings);
cljs.core.reset_BANG_(reagent.debug.warnings,null);

reagent.debug.tracking = false;

return warns;
});

//# sourceMappingURL=reagent.debug.js.map
