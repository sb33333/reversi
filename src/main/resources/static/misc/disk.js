export default {
    DARK:-1,
    LIGHT:1,
    getOpponent:function(side) {
        if (side === this.DARK) return this.LIGHT;
        else if (side === this.LIGHT) return this.DARK;
    },
}