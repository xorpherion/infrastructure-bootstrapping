<template>
    <div id="createObject">
        <div class="row">
            <div class="col-auto">
                <div v-for="obj in objects" :key="obj">
                    <button class="btn">{{obj}}s</button> 
                </div>
            </div>
            <div style="border-left: thin solid #b0b1b2;">
            </div>
            <div class="col-auto">
                <select multiple>
                </select>
            </div>
            <div style="border-left: thin solid #b0b1b2;">
            </div>
            <div class="col-auto">
                content
            </div>
        </div>
        
    </div>
</template>

<script>
    export default{
        name: "createObject",
        data () {
            return {
                objects: [
                    "Namespace",
                    "Cloud",

                ]
            }
        }
    }
</script>