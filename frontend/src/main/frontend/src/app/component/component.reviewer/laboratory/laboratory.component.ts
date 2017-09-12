import {Component} from "@angular/core";
import {LaboratoryComponent} from "../../component.admin/laboratory/laboratory.component";

@Component({
  selector: 'reviewerLaboratories',
  templateUrl: '../../component.admin/laboratory/laboratory.component.html'
})
export class ReviewerLaboratoryComponent extends LaboratoryComponent {

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/reviewerLaboratories', id]);
  }
}
