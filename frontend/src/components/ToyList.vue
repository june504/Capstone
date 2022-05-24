<template>

<v-data-table
    :headers="headers"
    :items="toyList"
    :items-per-page="5"
    class="elevation-1"
  ></v-data-table>

</template>

<script>
  const axios = require('axios').default;

  export default {
    name: 'ToyList',
    props: {
      value: Object,
      editMode: Boolean,
      isNew: Boolean
    },
    data: () => ({
        headers: [
            { text: "id", value: "id" },
            { text: "name", value: "name" },
            { text: "rentalId", value: "rentalId" },
            { text: "status", value: "status" },
            { text: "repairmanId", value: "repairmanId" },
        ],
        toyList : [],
    }),
    async created() {
      var temp = await axios.get(axios.backend + '/toylists')

      this.toyList = temp.data._embedded.toylists;

    },
    methods: {
    }
  }
</script>

