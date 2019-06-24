import { Component, Input, Output, EventEmitter, HostListener, OnInit } from '@angular/core';

import { DotContentTypeField } from '../models';
import { DotMessageService } from '@services/dot-messages-service';
import { FieldService } from '../service';
import { take } from 'rxjs/operators';

/**
 * This display field after being dropped into a Content Type Drop zone
 * @export
 * @class ContentTypesFieldDragabbleItemComponent
 */
@Component({
    selector: 'dot-content-type-field-dragabble-item',
    styleUrls: ['./content-type-field-dragabble-item.component.scss'],
    templateUrl: './content-type-field-dragabble-item.component.html'
})
export class ContentTypesFieldDragabbleItemComponent implements OnInit {
    @Input()
    field: DotContentTypeField;
    @Output()
    remove: EventEmitter<DotContentTypeField> = new EventEmitter();
    @Output()
    edit: EventEmitter<DotContentTypeField> = new EventEmitter();
    fieldAttributes: string;
    i18nMessages: {
        [key: string]: string;
    } = {};

    constructor(private dotMessageService: DotMessageService, public fieldService: FieldService) {}

    ngOnInit(): void {
        this.dotMessageService
            .getMessages([
                'contenttypes.field.atributes.required',
                'contenttypes.field.atributes.indexed',
                'contenttypes.field.atributes.listed',
                'contenttypes.action.edit',
                'contenttypes.action.delete'
            ])
            .pipe(take(1))
            .subscribe((res) => {
                this.i18nMessages = res;
                this.fieldAttributes = [
                    { name: this.field.fieldTypeLabel, value: !!this.field.fieldTypeLabel },
                    {
                        name: this.dotMessageService.get('contenttypes.field.atributes.required'),
                        value: this.field.required
                    },
                    {
                        name: this.dotMessageService.get('contenttypes.field.atributes.indexed'),
                        value: this.field.indexed
                    },
                    {
                        name: this.dotMessageService.get('contenttypes.field.atributes.listed'),
                        value: this.field.listed
                    }
                ]
                    .filter((field) => field.value)
                    .map((field) => field.name)
                    .join('&nbsp;&nbsp;&#8226;&nbsp;&nbsp;');
            });
    }

    @HostListener('click', ['$event'])
    onClick($event: MouseEvent) {
        $event.stopPropagation();
        this.edit.emit(this.field);
    }

    removeItem($event: MouseEvent): void {
        this.remove.emit(this.field);
        $event.stopPropagation();
    }
}
